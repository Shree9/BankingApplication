/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankapplicationrevised;

/**
 *
 * @author shree
 */
public abstract class Account {
    
    protected String name;

    abstract boolean withdraw(double amount);

    abstract boolean deposit(double amount);

    abstract double balance();
}
