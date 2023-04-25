import request from '@/utils/request'

// 创建合同
export function createContract(data) {
  return request({
    url: '/oa/contract/create',
    method: 'post',
    data: data
  })
}

// 更新合同
export function updateContract(data) {
  return request({
    url: '/oa/contract/update',
    method: 'put',
    data: data
  })
}

// 删除合同
export function deleteContract(id) {
  return request({
    url: '/oa/contract/delete?id=' + id,
    method: 'delete'
  })
}

// 获得合同
export function getContract(id) {
  return request({
    url: '/oa/contract/get?id=' + id,
    method: 'get'
  })
}

// 获得合同分页
export function getContractPage(query) {
  return request({
    url: '/oa/contract/page',
    method: 'get',
    params: query
  })
}

// 导出合同 Excel
export function exportContractExcel(query) {
  return request({
    url: '/oa/contract/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
