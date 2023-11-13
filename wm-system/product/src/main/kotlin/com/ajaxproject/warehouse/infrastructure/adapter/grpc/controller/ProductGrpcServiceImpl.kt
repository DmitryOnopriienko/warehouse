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
import com.ajaxproject.warehouse.infrastructure.adapter.common.service.CreateProductApiAdapter
import com.ajaxproject.warehouse.infrastructure.adapter.common.service.DeleteProductApiAdapter
import com.ajaxproject.warehouse.infrastructure.adapter.common.service.FindAllProductsApiAdapter
import com.ajaxproject.warehouse.infrastructure.adapter.common.service.GetProductByIdApiAdapter
import com.ajaxproject.warehouse.infrastructure.adapter.common.service.UpdateProductApiAdapter
import net.devh.boot.grpc.server.service.GrpcService
import reactor.core.publisher.Mono

@GrpcService
class ProductGrpcServiceImpl(
    private val findAllProductsApiAdapter: FindAllProductsApiAdapter,
    private val getProductByIdApiAdapter: GetProductByIdApiAdapter,
    private val createProductApiAdapter: CreateProductApiAdapter,
    private val updateProductApiAdapter: UpdateProductApiAdapter,
    private val deleteProductApiAdapter: DeleteProductApiAdapter,
) : ReactorProductGrpc.ProductImplBase() {

    override fun findAllProducts(request: FindAllProductsRequest): Mono<FindAllProductsResponse> =
        findAllProductsApiAdapter.findAll()

    override fun getProductById(request: GetProductByIdRequest): Mono<GetProductByIdResponse> =
        getProductByIdApiAdapter.getById(request.id)

    override fun createProduct(request: CreateProductRequest): Mono<CreateProductResponse> =
        createProductApiAdapter.createProduct(request)

    override fun updateProduct(request: UpdateProductRequest): Mono<UpdateProductResponse> =
        updateProductApiAdapter.updateProduct(request)

    override fun deleteProduct(request: DeleteProductByIdRequest): Mono<DeleteProductByIdResponse> =
        deleteProductApiAdapter.deleteProduct(request)
}
