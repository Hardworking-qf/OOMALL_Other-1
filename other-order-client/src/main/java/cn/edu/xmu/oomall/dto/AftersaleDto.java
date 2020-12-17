package cn.edu.xmu.oomall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author xincong yao
 * @date 2020-11-27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AftersaleDto implements Serializable {

    private Long orderId;
    private String orderSn;
    private Long skuId;
    private String skuName;
    private Long shopId;
    private Integer quantity;
}