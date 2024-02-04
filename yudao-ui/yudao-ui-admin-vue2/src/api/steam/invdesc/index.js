import request from '@/utils/request'

// 创建库存信息详情
export function createInvDesc(data) {
  return request({
    url: '/steam/inv-desc/create',
    method: 'post',
    data: data
  })
}

// 更新库存信息详情
export function updateInvDesc(data) {
  return request({
    url: '/steam/inv-desc/update',
    method: 'put',
    data: data
  })
}

// 删除库存信息详情
export function deleteInvDesc(id) {
  return request({
    url: '/steam/inv-desc/delete?id=' + id,
    method: 'delete'
  })
}

// 获得库存信息详情
export function getInvDesc(id) {
  return request({
    url: '/steam/inv-desc/get?id=' + id,
    method: 'get'
  })
}

// 获得库存信息详情分页
export function getInvDescPage(params) {
  return request({
    url: '/steam/inv-desc/page',
    method: 'get',
    params
  })
}
// 导出库存信息详情 Excel
export function exportInvDescExcel(params) {
  return request({
    url: '/steam/inv-desc/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}