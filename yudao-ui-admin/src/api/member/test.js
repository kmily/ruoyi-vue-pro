import request from '@/utils/request'

// 创建会员标签
export function createTest(data) {
  return request({
    url: '/member/test/create',
    method: 'post',
    data: data
  })
}

// 更新会员标签
export function updateTest(data) {
  return request({
    url: '/member/test/update',
    method: 'put',
    data: data
  })
}

// 删除会员标签
export function deleteTest(id) {
  return request({
    url: '/member/test/delete?id=' + id,
    method: 'delete'
  })
}

// 获得会员标签
export function getTest(id) {
  return request({
    url: '/member/test/get?id=' + id,
    method: 'get'
  })
}

// 获得会员标签分页
export function getTestPage(query) {
  return request({
    url: '/member/test/page',
    method: 'get',
    params: query
  })
}

// 导出会员标签 Excel
export function exportTestExcel(query) {
  return request({
    url: '/member/test/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
