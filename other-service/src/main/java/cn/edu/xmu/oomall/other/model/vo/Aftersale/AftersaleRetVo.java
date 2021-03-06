package cn.edu.xmu.oomall.other.model.vo.Aftersale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author XQChen
 * @version 创建时间：2020/12/16 上午10:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AftersaleRetVo {
    private Long id;
    private Long orderId;
    private Long orderItemId;
    private Long customerId;
    private Long shopId;
    private String serviceSn;
    private Byte type;
    private String reason;
    private Long refund;
    private Integer quantity;
    private Long regionId;
    private String detail;
    private String consignee;
    private String mobile;
    private String customerLogSn;
    private String shopLogSn;
    private Byte state;
}
