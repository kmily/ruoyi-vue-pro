import request from '@/utils/request'

// 创建产品反馈
export function createFeedback(data) {
  return request({
    url: '/oa/feedback/create',
    method: 'post',
    data: data
  })
}

// 更新产品反馈
export function updateFeedback(data) {
  return request({
    url: '/oa/feedback/update',
    method: 'put',
    data: data
  })
}

// 删除产品反馈
export function deleteFeedback(id) {
  return request({
    url: '/oa/feedback/delete?id=' + id,
    method: 'delete'
  })
}

// 获得产品反馈
export function getFeedback(id) {
  return request({
    url: '/oa/feedback/get?id=' + id,
    method: 'get'
  })
}

// 获得产品反馈分页
export function getFeedbackPage(query) {
  return request({
    url: '/oa/feedback/page',
    method: 'get',
    params: query
  })
}

// 导出产品反馈 Excel
export function exportFeedbackExcel(query) {
  return request({
    url: '/oa/feedback/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
