import request from '@/utils/request'

// 创建商机-跟进日志
export function createOpportunityFollowLog(data) {
  return request({
    url: '/oa/opportunity-follow-log/create',
    method: 'post',
    data: data
  })
}

// 更新商机-跟进日志
export function updateOpportunityFollowLog(data) {
  return request({
    url: '/oa/opportunity-follow-log/update',
    method: 'put',
    data: data
  })
}

// 删除商机-跟进日志
export function deleteOpportunityFollowLog(id) {
  return request({
    url: '/oa/opportunity-follow-log/delete?id=' + id,
    method: 'delete'
  })
}

// 获得商机-跟进日志
export function getOpportunityFollowLog(id) {
  return request({
    url: '/oa/opportunity-follow-log/get?id=' + id,
    method: 'get'
  })
}

// 获得商机-跟进日志分页
export function getOpportunityFollowLogPage(query) {
  return request({
    url: '/oa/opportunity-follow-log/page',
    method: 'get',
    params: query
  })
}

// 导出商机-跟进日志 Excel
export function exportOpportunityFollowLogExcel(query) {
  return request({
    url: '/oa/opportunity-follow-log/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
