package com.lagou.edu.diy.service;

/**
 * 自定义服务接口类
 *
 */
public interface ITransferServiceDIY {

    void transfer(String fromCardNo, String toCardNo, int money) throws Exception;
}
