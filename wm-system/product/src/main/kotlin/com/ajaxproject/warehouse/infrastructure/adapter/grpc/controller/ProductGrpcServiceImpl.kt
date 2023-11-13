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
import com.ajaxproject.warehouse.application.port.CreateProductApiInPort
import com.ajaxproject.warehouse.application.port.DeleteProductApiInPort
import com.ajaxproject.warehouse.application.port.FindAllProductsApiInPort
import com.ajaxproject.warehouse.application.port.GetProductByIdApiInPort
import com.ajaxproject.warehouse.application.port.UpdateProductApiInPort
import net.devh.boot.grpc.server.service.GrpcService
import reactor.core.publisher.Mono

@GrpcService
class ProductGrpcServiceImpl(
    private val findAllProductsApiInPort: FindAllProductsApiInPort,
    private val getProductByIdApiInPort: GetProductByIdApiInPort,
    private val createProductApiInPort: CreateProductApiInPort,
    private val updateProductApiInPort: UpdateProductApiInPort,
    private val deleteProductApiInPort: DeleteProductApiInPort,
) : ReactorProductGrpc.ProductImplBase() {

    override fun findAllProducts(request: FindAllProductsRequest): Mono<FindAllProductsResponse> =
        findAllProductsApiInPort.findAll()

    override fun getProductById(request: GetProductByIdRequest): Mono<GetProductByIdResponse> =
        getProductByIdApiInPort.getById(request.id)

    override fun createProduct(request: CreateProductRequest): Mono<CreateProductResponse> =
        createProductApiInPort.createProduct(request)

    override fun updateProduct(request: UpdateProductRequest): Mono<UpdateProductResponse> =
        updateProductApiInPort.updateProduct(request)

    override fun deleteProduct(request: DeleteProductByIdRequest): Mono<DeleteProductByIdResponse> =
        deleteProductApiInPort.deleteProduct(request)
}
