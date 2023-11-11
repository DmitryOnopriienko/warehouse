package com.ajaxproject.warehouse.infrastructure.adapter.grpc.controller

import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.CreateProductRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.CreateProductResponse
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.DeleteProductByIdRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.DeleteProductByIdResponse
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.FindAllProductsRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.FindAllProductsResponse
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.GetProductByIdRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.GetProductByIdResponse
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.UpdateProductRequest
import com.ajaxproject.api.internal.warehousesvc.input.reqreply.product.UpdateProductResponse
import com.ajaxproject.api.internal.warehousesvc.service.product.ReactorProductGrpc
import com.ajaxproject.warehouse.infrastructure.adapter.common.service.CreateProductApiService
import com.ajaxproject.warehouse.infrastructure.adapter.common.service.DeleteProductApiService
import com.ajaxproject.warehouse.infrastructure.adapter.common.service.FindAllProductsApiService
import com.ajaxproject.warehouse.infrastructure.adapter.common.service.GetProductByIdApiService
import com.ajaxproject.warehouse.infrastructure.adapter.common.service.UpdateProductApiService
import net.devh.boot.grpc.server.service.GrpcService
import reactor.core.publisher.Mono

@GrpcService
class ProductGrpcServiceImpl(
    private val findAllProductsApiService: FindAllProductsApiService,
    private val getProductByIdApiService: GetProductByIdApiService,
    private val createProductApiService: CreateProductApiService,
    private val updateProductApiService: UpdateProductApiService,
    private val deleteProductApiService: DeleteProductApiService,
) : ReactorProductGrpc.ProductImplBase() {

    override fun findAllProducts(request: FindAllProductsRequest): Mono<FindAllProductsResponse> =
        findAllProductsApiService.findAll()

    override fun getProductById(request: GetProductByIdRequest): Mono<GetProductByIdResponse> =
        getProductByIdApiService.getById(request.id)

    override fun createProduct(request: CreateProductRequest): Mono<CreateProductResponse> =
        createProductApiService.createProduct(request)

    override fun updateProduct(request: UpdateProductRequest): Mono<UpdateProductResponse> =
        updateProductApiService.updateProduct(request)

    override fun deleteProduct(request: DeleteProductByIdRequest): Mono<DeleteProductByIdResponse> =
        deleteProductApiService.deleteProduct(request)
}
