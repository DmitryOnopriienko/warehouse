package com.ajaxproject.warehouse.service

import com.ajaxproject.warehouse.dto.WaybillDataDto
import com.ajaxproject.warehouse.dto.WaybillDataLiteDto
import com.ajaxproject.warehouse.entity.Waybill
import com.ajaxproject.warehouse.entity.WaybillProduct
import com.ajaxproject.warehouse.exception.NotFoundException
import com.ajaxproject.warehouse.repository.WaybillProductRepository
import com.ajaxproject.warehouse.repository.WaybillRepository
import org.springframework.stereotype.Service

@Service
class WaybillServiceImpl(
    val waybillRepository: WaybillRepository,
    val waybillProductRepository: WaybillProductRepository
) : WaybillService {

    override fun findAll(): List<WaybillDataLiteDto> {
        return waybillRepository.findAll().map { it.mapToLiteDto() }
    }

    override fun findById(id: Int): WaybillDataDto {
        return waybillRepository.findById(id)
            .orElseThrow { NotFoundException("Waybill with id $id not found") }
            .mapToDataDto()
    }

    fun Waybill.mapToLiteDto(): WaybillDataLiteDto = WaybillDataLiteDto(
        id = id,
        customer = customer,
        date = date,
        totalPrice = countTotalPrice()
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

    fun Waybill.countTotalPrice(): Double = waybillProductRepository.findByWaybill(this)
            .asSequence()
            .map { it.product.price * it.amount }
            .sum()

    fun Waybill.findListOfProductsAndCountTotalPrice(): Pair<List<WaybillProduct>, Double> {
        val productList = waybillProductRepository.findByWaybill(this)
        val totalPrice = productList.asSequence()
            .map { it.product.price * it.amount }
            .sum()
        return productList to totalPrice
    }
}
