package org.jfantasy.express.rest;

import com.fantasy.framework.dao.Pager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.express.bean.Express;
import org.jfantasy.express.product.Logistics;
import org.jfantasy.express.product.LogisticsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "logisticses", description = "系统支持的物流查询")
@RestController
@RequestMapping("/logisticses")
public class LogisticsController {

    @Autowired
    private LogisticsConfiguration logisticsConfiguration;

    @ApiOperation(value = "按条件检索快递公司", notes = "筛选快递公司，返回通用分页对象", response = Express[].class)
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Logistics> search() {
        return this.logisticsConfiguration.getLogisticses();
    }

    @ApiOperation(value = "获取快递公司", notes = "通过该接口, 获取单篇快递公司", response = Express.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Logistics view(@PathVariable("id") String id) {
        return this.logisticsConfiguration.getLogistics(id);
    }

    @ApiOperation(value = "查询快递的物流信息", notes = "查询快递的物流信息")
    @RequestMapping(value = "/{id}/bills/{sn}", method = RequestMethod.GET)
    public void bill(@PathVariable("id") String id, @PathVariable("sn") String sn) {

    }

}
