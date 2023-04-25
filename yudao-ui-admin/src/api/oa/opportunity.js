import request from '@/utils/request'

// 创建商机
export function createOpportunity(data) {
  return request({
    url: '/oa/opportunity/create',
    method: 'post',
    data: data
  })
}

// 更新商机
export function updateOpportunity(data) {
  return request({
    url: '/oa/opportunity/update',
    method: 'put',
    data: data
  })
}

// 删除商机
export function deleteOpportunity(id) {
  return request({
    url: '/oa/opportunity/delete?id=' + id,
    method: 'delete'
  })
}

// 获得商机
export function getOpportunity(id) {
  return request({
    url: '/oa/opportunity/get?id=' + id,
    method: 'get'
  })
}

// 获得商机分页
export function getOpportunityPage(query) {
  return request({
    url: '/oa/opportunity/page',
    method: 'get',
    params: query
  })
}

// 导出商机 Excel
export function exportOpportunityExcel(query) {
  return request({
    url: '/oa/opportunity/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
