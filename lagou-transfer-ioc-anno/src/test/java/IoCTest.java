import com.lagou.edu.SpringConfig;
import com.lagou.edu.dao.AccountDao;
import com.lagou.edu.diy.service.ITransferServiceDIY;
import com.lagou.edu.diy.service.impl.TransferServiceDIYImpl;
import com.lagou.edu.diy.utils.AnnoConfigApplicationContextDIY;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author 应癫
 */
public class IoCTest {


    @Test
    public void testIoC() throws Exception {

        // 通过读取classpath下的xml文件来启动容器（xml模式SE应用下推荐）
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);

        AccountDao accountDao = (AccountDao) applicationContext.getBean("accountDao");

        System.out.println(accountDao);



    }


    @Test
    public void testAnnocationIoCDIY() throws Exception {

        // 读取自定义目录下指定注解类进入容器
        AnnoConfigApplicationContextDIY applicationContext = new AnnoConfigApplicationContextDIY("com.lagou.edu.diy");

        ITransferServiceDIY transferServiceDIY = (ITransferServiceDIY) applicationContext.getBean("transferServiceDIY");

        transferServiceDIY.transfer("6029621011000","6029621011001",100);



    }
}
