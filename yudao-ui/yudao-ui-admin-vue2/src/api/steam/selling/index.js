import request from '@/utils/request'

// 创建在售饰品
export function createSelling(data) {
  return request({
    url: '/steam/selling/create',
    method: 'post',
    data: data
  })
}

// 更新在售饰品
export function updateSelling(data) {
  return request({
    url: '/steam/selling/update',
    method: 'put',
    data: data
  })
}

// 删除在售饰品
export function deleteSelling(id) {
  return request({
    url: '/steam/selling/delete?id=' + id,
    method: 'delete'
  })
}

// 获得在售饰品
export function getSelling(id) {
  return request({
    url: '/steam/selling/get?id=' + id,
    method: 'get'
  })
}

// 获得在售饰品分页
export function getSellingPage(params) {
  return request({
    url: '/steam/selling/page',
    method: 'get',
    params
  })
}
// 导出在售饰品 Excel
export function exportSellingExcel(params) {
  return request({
    url: '/steam/selling/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}