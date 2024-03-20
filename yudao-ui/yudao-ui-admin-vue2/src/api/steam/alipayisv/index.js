import request from '@/utils/request'

// 创建签约ISV用户
export function createAlipayIsv(data) {
  return request({
    url: '/steam/alipay-isv/create',
    method: 'post',
    data: data
  })
}

// 更新签约ISV用户
export function updateAlipayIsv(data) {
  return request({
    url: '/steam/alipay-isv/update',
    method: 'put',
    data: data
  })
}

// 删除签约ISV用户
export function deleteAlipayIsv(id) {
  return request({
    url: '/steam/alipay-isv/delete?id=' + id,
    method: 'delete'
  })
}

// 获得签约ISV用户
export function getAlipayIsv(id) {
  return request({
    url: '/steam/alipay-isv/get?id=' + id,
    method: 'get'
  })
}

// 获得签约ISV用户分页
export function getAlipayIsvPage(params) {
  return request({
    url: '/steam/alipay-isv/page',
    method: 'get',
    params
  })
}
// 导出签约ISV用户 Excel
export function exportAlipayIsvExcel(params) {
  return request({
    url: '/steam/alipay-isv/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}