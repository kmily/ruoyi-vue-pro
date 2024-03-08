import request from '@/utils/request'

// 创建绑定用户IP地址池
export function createBindIpaddress(data) {
  return request({
    url: '/steam/bind-ipaddress/create',
    method: 'post',
    data: data
  })
}

// 更新绑定用户IP地址池
export function updateBindIpaddress(data) {
  return request({
    url: '/steam/bind-ipaddress/update',
    method: 'put',
    data: data
  })
}

// 删除绑定用户IP地址池
export function deleteBindIpaddress(id) {
  return request({
    url: '/steam/bind-ipaddress/delete?id=' + id,
    method: 'delete'
  })
}

// 获得绑定用户IP地址池
export function getBindIpaddress(id) {
  return request({
    url: '/steam/bind-ipaddress/get?id=' + id,
    method: 'get'
  })
}

// 获得绑定用户IP地址池分页
export function getBindIpaddressPage(params) {
  return request({
    url: '/steam/bind-ipaddress/page',
    method: 'get',
    params
  })
}
// 导出绑定用户IP地址池 Excel
export function exportBindIpaddressExcel(params) {
  return request({
    url: '/steam/bind-ipaddress/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}