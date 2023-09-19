/*
*  Copyright 2019-2023 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.service.impl;

import me.zhengjie.domain.Stock;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.service.StockService;
import me.zhengjie.domain.vo.StockQueryCriteria;
import me.zhengjie.mapper.StockMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import me.zhengjie.utils.PageUtil;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import me.zhengjie.utils.PageResult;

/**
* @description 服务实现
* @author rdp
* @date 2023-07-13
**/
@Service
@RequiredArgsConstructor
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {

    private final StockMapper stockMapper;

    @Override
    public PageResult<Stock> queryAll(StockQueryCriteria criteria, Page<Object> page){
        return PageUtil.toPage(stockMapper.findAll(criteria, page));
    }

    @Override
    public List<Stock> queryAll(StockQueryCriteria criteria){
        return stockMapper.findAll(criteria);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Stock resources) {
        save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Stock resources) {
        Stock stock = getById(resources.getId());
        stock.copy(resources);
        saveOrUpdate(stock);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(List<Long> ids) {
        removeBatchByIds(ids);
    }

    @Override
    public void download(List<Stock> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Stock stock : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", stock.getName());
            map.put("品牌", stock.getBrand());
            map.put("数量", stock.getQuantity());
            map.put("单价", stock.getPrice());
            map.put("金额", stock.getAmount());
            map.put("备注", stock.getRemark());
            map.put("创建者", stock.getCreateBy());
            map.put("更新者", stock.getUpdateBy());
            map.put("创建日期", stock.getCreateTime());
            map.put("更新时间", stock.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}