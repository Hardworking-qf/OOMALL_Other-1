package cn.edu.xmu.oomall.other.util.ServiceStub;

import cn.edu.xmu.goods.client.IGoodsService;
import cn.edu.xmu.goods.client.dubbo.*;

import java.nio.channels.SelectionKey;
import java.util.List;
import java.util.Map;

/**
 * 2 * @author: LiangJi3229
 * 3 * @date: 2020/12/14 下午7:49
 * 4
 */
public class GoodsService implements IGoodsService {

    @Override
    public Map<ShopDTO, List<OrderItemDTO>> classifySku(List<OrderItemDTO> orderItemDTOS) {
        return null;
    }

    @Override
    public SkuDTO getSku(Long skuId) {
        SkuDTO skuDTO=new SkuDTO();
        skuDTO.setId(1L);
        skuDTO.setName("桩商品");
        return skuDTO;
    }

    @Override
    public List<SkuDTO> getSkus(List<Long> skuIds) {
        return null;
    }

    @Override
    public SpuDTO getSimpleSpuById(Long spuId) {
        return null;
    }

    @Override
    public ShopDTO getShopBySKUId(Long skuId) {
        return new ShopDTO(1L);
    }

    @Override
    public Long getShopIdBySKUId(Long skuId) {
        return null;
    }

    @Override
    public Long getGoodWeightBySku(Long skuId) {
        return null;
    }

    @Override
    public Long getFreightModelIdBySku(Long skuID) {
        return null;
    }

    @Override
    public Boolean deleteFreightModelIdBySku(Long modelId, Long shopId) {
        return null;
    }

    @Override
    public List<PriceDTO> getPriceAndName(List<OrderItemDTO> orderItemDTOS) {
        return null;
    }

    @Override
    public PriceDTO getPrice(Long skuId) {
        return null;
    }


}
