import request from '@/utils/request'

// 创建开放平台用户
export function createDevAccount(data) {
  return request({
    url: '/steam/dev-account/create',
    method: 'post',
    data: data
  })
}

// 更新开放平台用户
export function updateDevAccount(data) {
  return request({
    url: '/steam/dev-account/update',
    method: 'put',
    data: data
  })
}

// 删除开放平台用户
export function deleteDevAccount(id) {
  return request({
    url: '/steam/dev-account/delete?id=' + id,
    method: 'delete'
  })
}

// 获得开放平台用户
export function getDevAccount(id) {
  return request({
    url: '/steam/dev-account/get?id=' + id,
    method: 'get'
  })
}

// 获得开放平台用户分页
export function getDevAccountPage(params) {
  return request({
    url: '/steam/dev-account/page',
    method: 'get',
    params
  })
}
// 导出开放平台用户 Excel
export function exportDevAccountExcel(params) {
  return request({
    url: '/steam/dev-account/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}