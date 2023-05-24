import request from '@/utils/request'

// 创建客户管理
export function createCustomer(data) {
  return request({
    url: '/oa/customer/create',
    method: 'post',
    data: data
  })
}

// 更新客户管理
export function updateCustomer(data) {
  return request({
    url: '/oa/customer/update',
    method: 'put',
    data: data
  })
}

// 删除客户管理
export function deleteCustomer(id) {
  return request({
    url: '/oa/customer/delete?id=' + id,
    method: 'delete'
  })
}

// 获得客户管理
export function getCustomer(id) {
  return request({
    url: '/oa/customer/get?id=' + id,
    method: 'get'
  })
}

// 获得客户管理分页
export function getCustomerPage(query) {
  return request({
    url: '/oa/customer/page',
    method: 'get',
    params: query
  })
}

// 导出客户管理 Excel
export function exportCustomerExcel(query) {
  return request({
    url: '/oa/customer/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
