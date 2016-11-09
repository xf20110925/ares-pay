/**
 * Description: PaymentVo.java
 * All Rights Reserved.
 * @version 1.0  2016年11月2日 下午6:27:49  by 王冠华（guanhua.wang@pintuibao.cn）创建
 */
package vo;

import java.io.Serializable;
import java.util.List;

/**
 * 支付方式VO
 * Description:
 * All Rights Reserved.
 * @version 1.0  2016年11月2日 下午6:27:49  by 王冠华（guanhua.wang@pintuibao.cn）创建
 */
public class PaymentVO implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1153953891004381684L;

    private int id;
    private String name;
    private List<PaymentVO> children;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the children
     */
    public List<PaymentVO> getChildren() {
        return children;
    }
    /**
     * @param children the children to set
     */
    public void setChildren(List<PaymentVO> children) {
        this.children = children;
    }


}
