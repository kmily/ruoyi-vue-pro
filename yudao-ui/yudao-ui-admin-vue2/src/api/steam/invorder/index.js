import request from '@/utils/request'

// 创建steam订单
export function createInvOrder(data) {
  return request({
    url: '/steam/inv-order/create',
    method: 'post',
    data: data
  })
}

// 更新steam订单
export function updateInvOrder(data) {
  return request({
    url: '/steam/inv-order/update',
    method: 'put',
    data: data
  })
}

// 删除steam订单
export function deleteInvOrder(id) {
  return request({
    url: '/steam/inv-order/delete?id=' + id,
    method: 'delete'
  })
}

// 获得steam订单
export function getInvOrder(id) {
  return request({
    url: '/steam/inv-order/get?id=' + id,
    method: 'get'
  })
}

// 获得steam订单分页
export function getInvOrderPage(params) {
  return request({
    url: '/steam/inv-order/page',
    method: 'get',
    params
  })
}
// 导出steam订单 Excel
export function exportInvOrderExcel(params) {
  return request({
    url: '/steam/inv-order/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}