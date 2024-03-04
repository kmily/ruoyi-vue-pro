import request from '@/utils/request'

// 创建steam订单
export function createYouyouOrder(data) {
  return request({
    url: '/steam/youyou-order/create',
    method: 'post',
    data: data
  })
}

// 更新steam订单
export function updateYouyouOrder(data) {
  return request({
    url: '/steam/youyou-order/update',
    method: 'put',
    data: data
  })
}

// 删除steam订单
export function deleteYouyouOrder(id) {
  return request({
    url: '/steam/youyou-order/delete?id=' + id,
    method: 'delete'
  })
}

// 获得steam订单
export function getYouyouOrder(id) {
  return request({
    url: '/steam/youyou-order/get?id=' + id,
    method: 'get'
  })
}

// 获得steam订单分页
export function getYouyouOrderPage(params) {
  return request({
    url: '/steam/youyou-order/page',
    method: 'get',
    params
  })
}
// 导出steam订单 Excel
export function exportYouyouOrderExcel(params) {
  return request({
    url: '/steam/youyou-order/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}