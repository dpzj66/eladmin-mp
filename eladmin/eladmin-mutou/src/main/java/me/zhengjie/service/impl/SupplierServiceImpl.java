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

import me.zhengjie.domain.Supplier;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.zhengjie.service.SupplierService;
import me.zhengjie.domain.vo.SupplierQueryCriteria;
import me.zhengjie.mapper.SupplierMapper;
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
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {

    private final SupplierMapper supplierMapper;

    @Override
    public PageResult<Supplier> queryAll(SupplierQueryCriteria criteria, Page<Object> page){
        return PageUtil.toPage(supplierMapper.findAll(criteria, page));
    }

    @Override
    public List<Supplier> queryAll(SupplierQueryCriteria criteria){
        return supplierMapper.findAll(criteria);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Supplier resources) {
        save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Supplier resources) {
        Supplier supplier = getById(resources.getId());
        supplier.copy(resources);
        saveOrUpdate(supplier);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(List<Long> ids) {
        removeBatchByIds(ids);
    }

    @Override
    public void download(List<Supplier> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Supplier supplier : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", supplier.getName());
            map.put("备注", supplier.getRemark());
            map.put("创建者", supplier.getCreateBy());
            map.put("更新者", supplier.getUpdateBy());
            map.put("创建日期", supplier.getCreateTime());
            map.put("更新时间", supplier.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}