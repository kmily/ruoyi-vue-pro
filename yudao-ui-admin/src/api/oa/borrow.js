import request from '@/utils/request'

// 创建借支申请
export function createBorrow(data) {
  return request({
    url: '/oa/borrow/create',
    method: 'post',
    data: data
  })
}

// 更新借支申请
export function updateBorrow(data) {
  return request({
    url: '/oa/borrow/update',
    method: 'put',
    data: data
  })
}

// 删除借支申请
export function deleteBorrow(id) {
  return request({
    url: '/oa/borrow/delete?id=' + id,
    method: 'delete'
  })
}

// 获得借支申请
export function getBorrow(id) {
  return request({
    url: '/oa/borrow/get?id=' + id,
    method: 'get'
  })
}

// 获得借支申请分页
export function getBorrowPage(query) {
  return request({
    url: '/oa/borrow/page',
    method: 'get',
    params: query
  })
}

// 导出借支申请 Excel
export function exportBorrowExcel(query) {
  return request({
    url: '/oa/borrow/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
