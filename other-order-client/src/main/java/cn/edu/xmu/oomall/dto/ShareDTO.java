package cn.edu.xmu.oomall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xincong yao
 * @date 2020-12-7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareDTO {

    private Long orderItemId;

    private Long customerId;

    private Long skuId;

    private Long beSharedId;
}