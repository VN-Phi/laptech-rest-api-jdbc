package com.laptech.restapi.controller;

import com.laptech.restapi.dto.response.BaseResponse;
import com.laptech.restapi.dto.response.DataResponse;
import com.laptech.restapi.model.Brand;
import com.laptech.restapi.service.BrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nhat Phi
 * @since 2022-11-18
 */
@Api(tags = "Brand of Product", value = "Brand Controller")
@CrossOrigin
@RestController
@RequestMapping("api/v1/brands")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @ApiOperation(value = "Get all brands in system", response = Brand.class)
    @GetMapping("")
    public ResponseEntity<DataResponse> getAllBrands(@RequestParam(required = false) String sortBy,
                                                     @RequestParam(required = false) String sortDir,
                                                     @RequestParam(required = false) Long page,
                                                     @RequestParam(required = false) Long size) {
        return DataResponse.getCollectionSuccess(
                "Get all brands",
                brandService.count(),
                brandService.findAll(sortBy, sortDir, page, size)
        );
    }

    @ApiOperation(value = "Get brand with filter", response = Brand.class)
    @GetMapping("filter")
    public ResponseEntity<DataResponse> getBrandWithFilter() {
        Map<String, Object> params = new HashMap<>();
        return DataResponse.getCollectionSuccess(
                "Get brand with filter",
                brandService.findWithFilter(params)
        );
    }

    @ApiOperation(value = "Get one brand in system with id", response = Brand.class)
    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<DataResponse> getBrandById(@PathVariable("id") long brandId) {
        return DataResponse.getObjectSuccess(
                "Get brand",
                brandService.findById(brandId)
        );
    }

    @ApiOperation(value = "Create a new brand", response = DataResponse.class)
    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<DataResponse> createNewBrand(@RequestBody Brand brand) {
        return DataResponse.success(
                "Create new brand",
                brandService.insert(brand)
        );
    }

    @ApiOperation(value = "Update a brand", response = BaseResponse.class)
    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<BaseResponse> updateBrand(@PathVariable("id") long brandId,
                                                    @RequestBody Brand brand) {
        brandService.update(brand, brandId);
        return DataResponse.success("Update brand");
    }

    @ApiOperation(value = "Delete brand in system", response = BaseResponse.class)
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<BaseResponse> deleteBrand(@PathVariable("id") long brandId,
                                                    @RequestBody(required = false) Map<String, String> body) {
        brandService.delete(brandId, (body != null) ? body.get("updateBy") : null);
        return DataResponse.success("Delete brand");
    }
}
