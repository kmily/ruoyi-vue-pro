import request from '@/utils/request'

// 创建提现
export function createWithdrawal(data) {
  return request({
    url: '/steam/withdrawal/create',
    method: 'post',
    data: data
  })
}

// 更新提现
export function updateWithdrawal(data) {
  return request({
    url: '/steam/withdrawal/update',
    method: 'put',
    data: data
  })
}

// 删除提现
export function deleteWithdrawal(id) {
  return request({
    url: '/steam/withdrawal/delete?id=' + id,
    method: 'delete'
  })
}

// 获得提现
export function getWithdrawal(id) {
  return request({
    url: '/steam/withdrawal/get?id=' + id,
    method: 'get'
  })
}

// 获得提现分页
export function getWithdrawalPage(params) {
  return request({
    url: '/steam/withdrawal/page',
    method: 'get',
    params
  })
}
// 导出提现 Excel
export function exportWithdrawalExcel(params) {
  return request({
    url: '/steam/withdrawal/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}