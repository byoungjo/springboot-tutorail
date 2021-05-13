package multi.database.db1.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import multi.database.model.Model;

public interface Db1Mapper {
	@Select(" select * from BILL_STATEMENTS where id = #{modelId} limit  1")
	public Model findModelByModelId(long modelId);

	@Insert("INSERT INTO multi_db_model (model_id) VALUES (#{modelId})")
	public int saveModel(Model model);
}

