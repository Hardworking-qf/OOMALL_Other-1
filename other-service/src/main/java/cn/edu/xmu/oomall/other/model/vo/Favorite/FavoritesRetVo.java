package cn.edu.xmu.oomall.other.model.vo.Favorite;

import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/2 下午5:12
 */
@Data
public class FavoritesRetVo {
    private Long id;
    Object goodsSku;
    private LocalDateTime gmtCreate;
}
