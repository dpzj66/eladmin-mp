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
package me.zhengjie.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.domain.Supplier;
import me.zhengjie.service.SupplierService;
import me.zhengjie.domain.vo.SupplierQueryCriteria;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.utils.PageResult;

/**
* @author rdp
* @date 2023-07-13
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "供应商管理")
@RequestMapping("/api/supplier")
public class SupplierController {

    private final SupplierService supplierService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('supplier:list')")
    public void exportSupplier(HttpServletResponse response, SupplierQueryCriteria criteria) throws IOException {
        supplierService.download(supplierService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询供应商")
    @ApiOperation("查询供应商")
    @PreAuthorize("@el.check('supplier:list')")
    public ResponseEntity<PageResult<Supplier>> querySupplier(SupplierQueryCriteria criteria, Page<Object> page){
        return new ResponseEntity<>(supplierService.queryAll(criteria,page),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增供应商")
    @ApiOperation("新增供应商")
    @PreAuthorize("@el.check('supplier:add')")
    public ResponseEntity<Object> createSupplier(@Validated @RequestBody Supplier resources){
        supplierService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改供应商")
    @ApiOperation("修改供应商")
    @PreAuthorize("@el.check('supplier:edit')")
    public ResponseEntity<Object> updateSupplier(@Validated @RequestBody Supplier resources){
        supplierService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除供应商")
    @ApiOperation("删除供应商")
    @PreAuthorize("@el.check('supplier:del')")
    public ResponseEntity<Object> deleteSupplier(@RequestBody List<Long> ids) {
        supplierService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}