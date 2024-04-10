import request from '@/utils/request'

// 创建其他平台在售
export function createOtherSelling(data) {
  return request({
    url: '/steam/other-selling/create',
    method: 'post',
    data: data
  })
}

// 更新其他平台在售
export function updateOtherSelling(data) {
  return request({
    url: '/steam/other-selling/update',
    method: 'put',
    data: data
  })
}

// 删除其他平台在售
export function deleteOtherSelling(id) {
  return request({
    url: '/steam/other-selling/delete?id=' + id,
    method: 'delete'
  })
}

// 获得其他平台在售
export function getOtherSelling(id) {
  return request({
    url: '/steam/other-selling/get?id=' + id,
    method: 'get'
  })
}

// 获得其他平台在售分页
export function getOtherSellingPage(params) {
  return request({
    url: '/steam/other-selling/page',
    method: 'get',
    params
  })
}
// 导出其他平台在售 Excel
export function exportOtherSellingExcel(params) {
  return request({
    url: '/steam/other-selling/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}