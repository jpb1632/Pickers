package springBootPickers.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;    
import org.slf4j.LoggerFactory; 

import springBootPickers.mapper.AutoNumMapper;

@Service
public class AutoNumService {
    
    
    private static final Logger log = LoggerFactory.getLogger(AutoNumService.class);

    @Autowired
    AutoNumMapper autoNumMapper;
    
    public String execute(String sep, String columnName, Integer len, String tableName) {
        String autoNum = autoNumMapper.AutoNumSelect(sep, columnName, len, tableName);
        
        if (autoNum == null || autoNum.isEmpty()) {          
            log.error("AutoNum 생성 실패 - Table: {}, Column: {}", tableName, columnName);
        } else {
            log.debug("Generated AutoNum: {}", autoNum);
        }        
        return autoNum;
    }
}