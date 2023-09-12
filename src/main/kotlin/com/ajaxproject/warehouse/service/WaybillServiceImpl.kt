package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.WaybillCreateDto
import com.ajaxproject.warehouse.dto.WaybillDataDto
import com.ajaxproject.warehouse.dto.WaybillDataLiteDto
import com.ajaxproject.warehouse.entity.Product
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

    override fun createWaybill(createDto: WaybillCreateDto): WaybillDataDto {
        val waybill = createDto.mapToEntity()
        if (createDto.products == null) {
            return waybillRepository.save(waybill).mapToDataDto()
        }
        val errorList: MutableList<String> = mutableListOf()
        val products: MutableList<Pair<Product, Int>> = mutableListOf()
        for (productDto in createDto.products) {
            productRepository.findById(productDto.id as Int).ifPresentOrElse(
                { products.add(it to productDto.amount as Int) },
                { errorList.add("Product with id ${productDto.id} not found") }
            )
        }
        if (errorList.isNotEmpty()) {
            throw NotFoundException(errorList)
        }
        waybillRepository.save(waybill)
        products.forEach { (product, amount) ->
            waybillProductRepository.saveProductAndChangeAmount(waybill, product, amount)
        }
        return waybill.mapToDataDto()
    }

    private fun WaybillProductRepository.saveProductAndChangeAmount(
        waybill: Waybill,
        product: Product,
        amount: Int
    ) {
        val waybillProduct = WaybillProduct(
            id = WaybillProduct.WaybillProductPK(
                waybillId = waybill.id as Int,
                productId = product.id as Int
            ),
            product = product,
            waybill = waybill,
            amount = amount
        )
        save(waybillProduct)
        product.amount -= amount
        productRepository.save(product)
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
