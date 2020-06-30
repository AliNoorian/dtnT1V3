package com.dotin;

import com.dotin.accountPKG.AccountDTO;
import com.dotin.balancePKG.BalanceDTO;
import com.dotin.payPKG.PaymentDTO;
import com.dotin.transactionPKG.TransactionDTO;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        List<AccountDTO> accountList = new ArrayList<>();
        List<String> payListString = new ArrayList<>();
        List<String> balanceListString = new ArrayList<>();
        List<String> transactionListString = new ArrayList<>();
        List<String> accountListString = new ArrayList<>();
        String[] accountString = null;
        Random rand = new Random();

        try {
            File fileDir = new File("account.txt");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(fileDir), "UTF8"));
            String str;
            while ((str = in.readLine()) != null) {
                AccountDTO newAccount = new AccountDTO();
                accountString = (str.split("\t"));
                newAccount.setDepositNumber(accountString[0]);
                BigDecimal bd = new BigDecimal(accountString[1]);
                newAccount.setAmount(bd);
                accountList.add(newAccount);
            }
            in.close();

        } catch (Throwable t) {
            AccountDTO newAccount = new AccountDTO();
            AccountDTO newAccount2 = new AccountDTO();

            String randomDepositNumber1 = "1.10.100.1";
            newAccount2.setDepositNumber(randomDepositNumber1);
            BigDecimal bigDecimal2 = new BigDecimal((rand.nextInt(1000)));
            newAccount2.setAmount(bigDecimal2);
            accountListString.add(newAccount2.toString());

            int accountLoopLength = (rand.nextInt(100));
            for (int i = 0; i <= accountLoopLength; i++) {
                String randomDepositNumber2 = "1.20.100." + (rand.nextInt(200));
                Optional<AccountDTO> first = accountList.stream()
                        .filter(x -> Objects.equals(randomDepositNumber2, x.getDepositNumber()))
                        .findFirst();
                if (!(first.isPresent())) {
                    newAccount.setDepositNumber(randomDepositNumber2);
                    BigDecimal bigDecimal = new BigDecimal((rand.nextInt(50)));
                    newAccount.setAmount(bigDecimal);
                    accountListString.add(newAccount.toString());
                }
            }
            saveFile("account", accountListString);
        }

        int payLoopLength = rand.nextInt(100);
        for (int j = 0; j <= payLoopLength; j++) {
            String inputDepositNumber = "1.10.100.1";
            Optional<AccountDTO> first = accountList.stream()
                    .filter(x -> Objects.equals(inputDepositNumber, x.getDepositNumber()))
                    .findFirst();
            //find deposit number that you want to pay
            if (first.isPresent()) {
                //pay(deptor)
                PaymentDTO newPay = new PaymentDTO();
                newPay.setDeptorOrCreditor("debtor");
                newPay.setDepositNumber("1.10.100.1");
                newPay.setAmount(first.get().getAmount());

                //pay(creditor)
                PaymentDTO newPay2 = new PaymentDTO();
                String inputDepositNumber2 = "1.20.100." + (rand.nextInt(200));
                Optional<AccountDTO> first2 = accountList.stream()
                        .filter(x -> Objects.equals(inputDepositNumber2, x.getDepositNumber()))
                        .findFirst();

                //find account(creditor) in account
                if (first2.isPresent()) {
                    //    System.out.println("found");
                    newPay2.setDeptorOrCreditor("creditor");
                    newPay2.setDepositNumber(inputDepositNumber2);
                    BigDecimal bigDecimal;
                    bigDecimal = new BigDecimal(rand.nextInt(100));

                    if (!(accountList.get(accountList.indexOf(first.get())).getAmount().compareTo(bigDecimal) < 0)) {
                        accountList.get(accountList.indexOf(first.get())).setAmount(first.get().getAmount().subtract(bigDecimal));
                        accountList.get(accountList.indexOf(first2.get())).setAmount(first2.get().getAmount().add(bigDecimal));
                        newPay2.setAmount(bigDecimal);

                        //save deposit number that you want to pay(deptor) in to the pay list
                        payListString.add(newPay.toString());

                        //save deposit number that you want to pay(deptor) yin to the balance list
                        BalanceDTO newBalance = new BalanceDTO(first.get().getDepositNumber(), first.get().getAmount());
                        balanceListString.add(newBalance.toString());

                        //save deposit number that give pay (creditor) in to the balance list
                        BalanceDTO newBalance2 = new BalanceDTO(first2.get().getDepositNumber(), bigDecimal);
                        balanceListString.add(newBalance2.toString());

                        //save transactions in to the transaction list list
                        TransactionDTO newTransaction = new TransactionDTO(first.get().getDepositNumber(), first2.get().getDepositNumber(), bigDecimal);
                        transactionListString.add(newTransaction.toString());

                        //save deposit number that give pay (creditor) in to the pay list
                        payListString.add(newPay2.toString());
                    }
                }
            }
        }
        for (AccountDTO accounts : accountList) {
            accountListString.add(accounts.toString());
        }
        saveFile("account", accountListString);
        saveFile("pay", payListString);
        saveFile("balance", balanceListString);
        saveFile("transaction", transactionListString);
    }

    public static void saveFile(String fileName, List<String> listName) throws IOException {
        File fileDir = new File(fileName + ".txt");
        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(fileDir), "UTF8"));
        for (String names : listName) {
            out.append(names);
        }
        out.flush();
        out.close();
    }
}
