package multi.database.service;

import lombok.extern.slf4j.Slf4j;
import multi.database.model.Employ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import multi.database.db1.dao.Db1Mapper;
import multi.database.db2.dao.Db2Mapper;
import multi.database.model.Model;
@Slf4j
@Service("withMapperService")
public class WithMapperService {
	private static final Logger logger = LoggerFactory.getLogger(WithMapperService.class);

	@Autowired
	Db1Mapper db1Mapper;
	
	@Autowired
	Db2Mapper db2Mapper;

	public Employ moveDb1toDb2(long modelId) {
		Model model = db1Mapper.findModelByModelId(modelId);
		log.info("db1Mapper getFirstName() {}", model.getFirstName());
		Employ employ = db2Mapper.findEmployId("S0000752");
		log.info("getEmpKornNm {}", employ.getEmpKornNm());
//		return db2Mapper.saveModel(model);
		return employ;
	}
}
