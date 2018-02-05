/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankapplicationrevised;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shree
 */
public class SavingsAccount extends Account {
    
    private double total;
    private final double minBalance = 10;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String status = "nothing";
    private double amount;
    private double balance;
    
    public SavingsAccount(String name, double total){
        this.name = name;
        File file = new File(this.name+"'sSavingsAmount.bin");
        if(file.exists()){
            try{
               readFromFile(file);
               balance = this.total;
            }
            catch(Exception e){
                
            }
        } else{
            this.total = total;
            balance = this.total;
        }          
    }
     public void readFromFile(File file) throws IOException, ClassNotFoundException{      
       try{
                ois = new ObjectInputStream(new FileInputStream(file));
                this.total = (double) ois.readObject();
                System.out.println("Value" + total);
                ois.close();
       } 
       catch(Exception e){
           
       }
       
     }
     
     @Override
    public boolean withdraw(double amount) {
        this.status = "withdrawn";
        this.amount = amount;
        if (!(amount > 0 && (balance - amount >= this.minBalance))) {
            return false;
        } else {
            balance -= amount;
            try {
                transactionSummary();
            } catch (IOException ex) {
                Logger.getLogger(CheckingsAccount.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }
    }

    @Override
    public boolean deposit(double amount) {
        this.amount = amount;
        this.status = "deposited";
        if (!(amount > 0)) {
            return false;
        } else {
           balance += amount;
            try {
                transactionSummary();
            } catch (IOException ex) {
                Logger.getLogger(CheckingsAccount.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }
       
    }

    @Override
    public double balance() {
         try {
            transactionSummary();
        } catch (IOException ex) {
            Logger.getLogger(CheckingsAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return balance;
    }

    public void writeToFile() {
        try {
            oos = new ObjectOutputStream(new FileOutputStream(new File(this.name+"'sSavingsAmount.bin")));
            oos.writeObject(this.balance());
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(BankApplicationRevised.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void transactionSummary() throws IOException{
         File transactions = new File(this.name+"'sSavingTransactionSummary.txt");
         FileWriter fw = new FileWriter(transactions);
         PrintWriter pw = new PrintWriter(fw);
         
        pw.println("Account Holders Name:" +name);
        pw.println("Account Type: Savings" );
        pw.println("Total Amount ="+ this.total);
        pw.println("Tranactions: ");
        if(this.status.equalsIgnoreCase("withdrawn")){
        pw.printf("%f was %s from the account", this.amount , this.status);
        }
        else if(this.status.equalsIgnoreCase("deposited")){
        pw.printf("%f was %s from the account",this.amount,this.status); 
        }
        pw.print("\tBalance"+ this.balance());
        
//        total = balance;
        
        pw.close();
    }
       
}
