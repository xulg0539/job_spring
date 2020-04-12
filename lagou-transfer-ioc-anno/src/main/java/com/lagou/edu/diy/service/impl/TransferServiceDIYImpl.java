package com.lagou.edu.diy.service.impl;


import com.lagou.edu.annocations.Autowired;
import com.lagou.edu.annocations.Service;

import com.lagou.edu.annocations.Transactional;
import com.lagou.edu.diy.dao.AccountDao;
import com.lagou.edu.pojo.Account;
import com.lagou.edu.diy.service.ITransferServiceDIY;

/**
 * 自定义服务实现类
 */
@Transactional
@Service("transferServiceDIY")
public class TransferServiceDIYImpl implements ITransferServiceDIY {


    // 最佳状态
    // @Autowired 按照类型注入 ,如果按照类型无法唯一锁定对象，可以结合@Qualifier指定具体的id
    @Autowired
    private AccountDao accountDao;


    @Override
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {

        /*try{
            // 开启事务(关闭事务的自动提交)
            TransactionManager.getInstance().beginTransaction();*/

        Account from = accountDao.queryAccountByCardNo(fromCardNo);
        Account to = accountDao.queryAccountByCardNo(toCardNo);

        from.setMoney(from.getMoney() - money);
        to.setMoney(to.getMoney() + money);

        accountDao.updateAccountByCardNo(to);
//        int c = 1/0;
        accountDao.updateAccountByCardNo(from);

        /*    // 提交事务

            TransactionManager.getInstance().commit();
        }catch (Exception e) {
            e.printStackTrace();
            // 回滚事务
            TransactionManager.getInstance().rollback();

            // 抛出异常便于上层servlet捕获
            throw e;

        }*/


    }
}
