package cn.iocoder.yudao.module.therapy.taskflow;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Engine {
//    private static ProcessEngine processEngine;

//    static {
//        processEngine = ProcessEngineConfiguration
//                .createStandaloneProcessEngineConfiguration()
//                .setJdbcUrl("jdbc:mysql://39.100.65.163:3306/hlgyy_icbt_db?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&nullCatalogMeansCurrent=true")
//                .setJdbcUsername("hlgyy")
//                .setJdbcPassword("cji13dxaici234dca")
//                .setJdbcDriver("com.mysql.cj.jdbc.Driver")
//                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
//                .buildProcessEngine();
//    }


//    public ProcessEngine getEngine(){
//        return processEngine;
//    }


    private final ProcessEngine processEngine;


    @Autowired
    public Engine(ProcessEngine processEngine){
        this.processEngine = processEngine;
    }


    public ProcessEngine getEngine(){
        if(this.processEngine == null){
            throw new RuntimeException("processEngine is null");
        }
        return processEngine;
    }
}