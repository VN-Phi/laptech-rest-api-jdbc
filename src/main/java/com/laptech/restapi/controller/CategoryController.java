package com.laptech.restapi.controller;

import com.laptech.restapi.dto.response.BaseResponse;
import com.laptech.restapi.dto.response.DataResponse;
import com.laptech.restapi.model.Category;
import com.laptech.restapi.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nhat Phi
 * @since 2022-11-24
 */
@Api(tags = "Type of Product", value = "Category controller")
@CrossOrigin(value = {"*"})
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "Get all categories in system", response = Category.class)
    @GetMapping("")
    public ResponseEntity<DataResponse> getAllCategories(@RequestParam(required = false) String sortBy,
                                                         @RequestParam(required = false) String sortDir,
                                                         @RequestParam(required = false) Long page,
                                                         @RequestParam(required = false) Long size) {
        return DataResponse.getCollectionSuccess(
                "Get all categories",
                categoryService.count(),
                categoryService.findAll(sortBy, sortDir, page, size)
        );
    }

    @ApiOperation(value = "Get categories with filter", response = Category.class)
    @GetMapping("filter")
    public ResponseEntity<DataResponse> getCategoryWithFilter() {
        Map<String, Object> params = new HashMap<>();
        return DataResponse.getCollectionSuccess(
                "Get all categories",
                categoryService.findWithFilter(params)
        );
    }

    @ApiOperation(value = "Get one category in system", response = Category.class)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<DataResponse> getCategoryById(@PathVariable("id") long categoryId) {
        return DataResponse.getObjectSuccess(
                "Get category",
                categoryService.findById(categoryId)
        );
    }

    @ApiOperation(value = "Create a new category", response = DataResponse.class)
    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<DataResponse> createNewCategory(@RequestBody Category category) {
        return DataResponse.success("Create new category",categoryService.insert(category));
    }

    @ApiOperation(value = "Update a category", response = BaseResponse.class)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<BaseResponse> updateCategory(@PathVariable("id") long categoryId, @RequestBody Category category) {
        categoryService.update(category, categoryId);
        return DataResponse.success("Update category");
    }

    @ApiOperation(value = "Remove category", response = BaseResponse.class)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<BaseResponse> deleteCategory(@PathVariable("id") long categoryId,
                                                       @RequestBody(required = false) Map<String, String> body) {
        categoryService.delete(categoryId, (body != null) ? body.get("updateBy") : null);
        return DataResponse.success("Delete category");
    }
}
