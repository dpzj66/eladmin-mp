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
import me.zhengjie.domain.Stock;
import me.zhengjie.service.StockService;
import me.zhengjie.domain.vo.StockQueryCriteria;
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
@Api(tags = "进货管理")
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('stock:list')")
    public void exportStock(HttpServletResponse response, StockQueryCriteria criteria) throws IOException {
        stockService.download(stockService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询进货")
    @ApiOperation("查询进货")
    @PreAuthorize("@el.check('stock:list')")
    public ResponseEntity<PageResult<Stock>> queryStock(StockQueryCriteria criteria, Page<Object> page){
        return new ResponseEntity<>(stockService.queryAll(criteria,page),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增进货")
    @ApiOperation("新增进货")
    @PreAuthorize("@el.check('stock:add')")
    public ResponseEntity<Object> createStock(@Validated @RequestBody Stock resources){
        stockService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改进货")
    @ApiOperation("修改进货")
    @PreAuthorize("@el.check('stock:edit')")
    public ResponseEntity<Object> updateStock(@Validated @RequestBody Stock resources){
        stockService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除进货")
    @ApiOperation("删除进货")
    @PreAuthorize("@el.check('stock:del')")
    public ResponseEntity<Object> deleteStock(@RequestBody List<Long> ids) {
        stockService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}