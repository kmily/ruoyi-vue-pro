import request from '@/utils/request'

// 创建steam用户库存储
export function createInv(data) {
  return request({
    url: '/steam/inv/create',
    method: 'post',
    data: data
  })
}

// 更新steam用户库存储
export function updateInv(data) {
  return request({
    url: '/steam/inv/update',
    method: 'put',
    data: data
  })
}

// 删除steam用户库存储
export function deleteInv(id) {
  return request({
    url: '/steam/inv/delete?id=' + id,
    method: 'delete'
  })
}

// 获得steam用户库存储
export function getInv(id) {
  return request({
    url: '/steam/inv/get?id=' + id,
    method: 'get'
  })
}

// 获得steam用户库存储分页
export function getInvPage(params) {
  return request({
    url: '/steam/inv/page',
    method: 'get',
    params
  })
}
// 导出steam用户库存储 Excel
export function exportInvExcel(params) {
  return request({
    url: '/steam/inv/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}