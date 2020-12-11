package cn.edu.xmu.oomall.other.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xincong yao
 * @date 2020-11-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
	private Long id;
	private CustomerDTO customer;
	private ShopDTO shop;
	private String orderSn;
	private List<OrderDTO> subOrders;
	private Long pid;
	private String consignee;
	private Long regionId;
	private String address;
	private String mobile;
	private String message;
	private Integer orderType;
	private Long freightPrice;
	private Long couponId;
	private Long couponActivityId;
	private Long discountPrice;
	private Long originPrice;
	private Long presaleId;
	private Long grouponId;
	private Long grouponDiscount;
	private Integer rebateNum;
	private LocalDateTime confirmTime;
	private String shipmentSn;
	private Integer state;
	private Integer subState;
	private Integer beDeleted;
	private List<OrderItemDTO> orderItems;
	private LocalDateTime gmtCreated;
	private LocalDateTime gmtModified;
}