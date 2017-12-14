// IPayService.aidl
package com.sty.aidl.remote.service;

// Declare any non-default types here with import statements

interface IPayService {
    boolean callPay(String name, String pwd, int money);
}
