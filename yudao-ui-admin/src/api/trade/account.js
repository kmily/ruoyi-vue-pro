import request from '@/utils/request'

// 创建交易账号
export function createAccount(data) {
  return request({
    url: '/trade/account/create',
    method: 'post',
    data: data
  })
}

// 更新交易账号
export function updateAccount(data) {
  return request({
    url: '/trade/account/update',
    method: 'put',
    data: data
  })
}

// 删除交易账号
export function deleteAccount(id) {
  return request({
    url: '/trade/account/delete?id=' + id,
    method: 'delete'
  })
}

// 获得交易账号
export function getAccount(id) {
  return request({
    url: '/trade/account/get?id=' + id,
    method: 'get'
  })
}

// 获得交易账号分页
export function getAccountPage(query) {
  return request({
    url: '/trade/account/page',
    method: 'get',
    params: query
  })
}

// 导出交易账号 Excel
export function exportAccountExcel(query) {
  return request({
    url: '/trade/account/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

export function syncBalanceApi(id) {
  return request({
    url: '/trade/account/sync/balance?id=' + id,
    method: 'get'
  })
}
