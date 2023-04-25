import request from '@/utils/request'

// 创建注册申请
export function createSignReq(data) {
  return request({
    url: '/system/sign-req/create',
    method: 'post',
    data: data
  })
}

// 更新注册申请
export function updateSignReq(data) {
  return request({
    url: '/system/sign-req/update',
    method: 'put',
    data: data
  })
}

// 删除注册申请
export function deleteSignReq(id) {
  return request({
    url: '/system/sign-req/delete?id=' + id,
    method: 'delete'
  })
}

// 获得注册申请
export function getSignReq(id) {
  return request({
    url: '/system/sign-req/get?id=' + id,
    method: 'get'
  })
}

// 获得注册申请分页
export function getSignReqPage(query) {
  return request({
    url: '/system/sign-req/page',
    method: 'get',
    params: query
  })
}

// 导出注册申请 Excel
export function exportSignReqExcel(query) {
  return request({
    url: '/system/sign-req/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
