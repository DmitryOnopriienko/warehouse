package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.WaybillCreateDto
import com.ajaxproject.warehouse.dto.WaybillDataDto
import com.ajaxproject.warehouse.dto.WaybillDataLiteDto
import com.ajaxproject.warehouse.entity.Waybill
import com.ajaxproject.warehouse.entity.WaybillProduct
import com.ajaxproject.warehouse.exception.NotFoundException
import com.ajaxproject.warehouse.repository.CustomerRepository
import com.ajaxproject.warehouse.repository.ProductRepository
import com.ajaxproject.warehouse.repository.WaybillProductRepository
import com.ajaxproject.warehouse.repository.WaybillRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class WaybillServiceImpl(
    val waybillRepository: WaybillRepository,
    val customerRepository: CustomerRepository,
    val waybillProductRepository: WaybillProductRepository,
    val productRepository: ProductRepository
) : WaybillService {

    override fun findAll(): List<WaybillDataLiteDto> {
        return waybillRepository.findAll().map { it.mapToLiteDto() }
    }

    override fun findById(id: Int): WaybillDataDto {
        return waybillRepository.findById(id)
            .orElseThrow { NotFoundException("Waybill with id $id not found") }
            .mapToDataDto()
    }

    override fun createWaybill(createDto: WaybillCreateDto) {
        val waybill = createDto.mapToEntity()
        waybillRepository.save(waybill)
        if (createDto.products != null) {
            val errorList: MutableList<String> = mutableListOf()
            for (productDto in createDto.products) {
                productRepository.findById(productDto.id as Int).ifPresentOrElse(
                    // TODO read about with, let, apply, also, run
                    { product ->    // TODO simplify or divide in several functions
                        waybillProductRepository.save(WaybillProduct(
                            id = WaybillProduct.WaybillProductPK(
                                waybillId = waybill.id as Int,
                                productId = product.id as Int
                            ),
                            product = product,
                            waybill = waybill,
                            amount = productDto.amount as Int
                        ))
                        product.amount -= productDto.amount
                        productRepository.save(product)
                    },
                    { errorList.add("Product with id ${productDto.id} not found") }
                )
            }
            // TODO add validation before saving
            if (errorList.isNotEmpty()) {
                throw NotFoundException(errorList)
            }
        }
    }

    fun Waybill.mapToLiteDto(): WaybillDataLiteDto = WaybillDataLiteDto(
        id = id,
        customer = customer,
        date = date,
        totalPrice = findListOfProductsAndCountTotalPrice().second
    )

    fun Waybill.mapToDataDto(): WaybillDataDto {
        val (productList, totalPrice) = this.findListOfProductsAndCountTotalPrice()
        return WaybillDataDto(
            id = id,
            customer = customer,
            date = date,
            productList = productList,
            totalPrice = totalPrice
        )
    }

    fun WaybillCreateDto.mapToEntity(): Waybill = Waybill(
        id = null,
        customer = customerRepository.findById(customerId as Int)
                .orElseThrow { NotFoundException("Customer with id $customerId not found") },
        date = date as LocalDate
    )

    fun Waybill.findListOfProductsAndCountTotalPrice(): Pair<List<WaybillProduct>, Double> {
        val productList = waybillProductRepository.findByWaybill(this)
        val totalPrice = productList.asSequence()
            .map { it.product.price * it.amount }
            .sum()
        return productList to totalPrice
    }
}
