package com.ajaxproject.warehouse.controller.grpc

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
import com.ajaxproject.warehouse.service.grpc.CreateProductGrpcService
import com.ajaxproject.warehouse.service.grpc.DeleteProductGrpcService
import com.ajaxproject.warehouse.service.grpc.FindAllProductsGrpcService
import com.ajaxproject.warehouse.service.grpc.GetProductByIdGrpcService
import com.ajaxproject.warehouse.service.grpc.UpdateProductGrpcService
import net.devh.boot.grpc.server.service.GrpcService
import reactor.core.publisher.Mono

@GrpcService
class ProductGrpcServiceImpl(
    val findAllProductsGrpcService: FindAllProductsGrpcService,
    val getProductByIdGrpcService: GetProductByIdGrpcService,
    val createProductGrpcService: CreateProductGrpcService,
    val updateProductGrpcService: UpdateProductGrpcService,
    val deleteProductGrpcService: DeleteProductGrpcService
) : ReactorProductGrpc.ProductImplBase() {

    override fun findAllProducts(request: FindAllProductsRequest): Mono<FindAllProductsResponse> =
        findAllProductsGrpcService.findAll()

    override fun getProductById(request: GetProductByIdRequest): Mono<GetProductByIdResponse> =
        getProductByIdGrpcService.getById(request.id)

    override fun createProduct(request: CreateProductRequest): Mono<CreateProductResponse> =
        createProductGrpcService.createProduct(request)

    override fun updateProduct(request: UpdateProductRequest): Mono<UpdateProductResponse> =
        updateProductGrpcService.updateProduct(request)

    override fun deleteProduct(request: DeleteProductByIdRequest): Mono<DeleteProductByIdResponse> =
        deleteProductGrpcService.deleteProduct(request)
}
