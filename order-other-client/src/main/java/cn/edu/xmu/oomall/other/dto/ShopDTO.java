package cn.edu.xmu.oomall.other.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xincong yao
 * @date 2020-11-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopDTO {
	private Long id;
	private String name;
	private String gmtCreateTime;
	private String gmtModiTime;
}