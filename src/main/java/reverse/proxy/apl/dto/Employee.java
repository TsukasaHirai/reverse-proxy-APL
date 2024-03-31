package reverse.proxy.apl.dto;

import java.sql.Timestamp;
import java.util.UUID;

import com.google.cloud.spring.data.spanner.core.mapping.Column;
import com.google.cloud.spring.data.spanner.core.mapping.PrimaryKey;
import com.google.cloud.spring.data.spanner.core.mapping.Table;

import lombok.Data;

@Data
@Table(name = "EMPLOYEE")
public class Employee {
    
	@PrimaryKey(1)
	@Column(name = "EMPLOYEE_UUID", nullable = false)
    private String employeeuuid;

	@PrimaryKey(2)
    @Column(name = "USER_NAME", nullable = false)
    private String username;
    
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    
    @Column(name = "CREATED_AT", nullable = false, spannerCommitTimestamp = true)
    private Timestamp createdAt;
    
    public Employee() {
    	// Spannerは@GeneratedValueを使用してIDを自動生成できないため、コンストラクタで自動生成 
    	// Spanner側のdefaul句もSpannerRepositoryからは使用することができない
    	this.employeeuuid = UUID.randomUUID().toString();
    }
}
