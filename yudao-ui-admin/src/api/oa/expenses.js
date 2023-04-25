import request from '@/utils/request'

// 创建报销申请
export function createExpenses(data) {
  return request({
    url: '/oa/expenses/create',
    method: 'post',
    data: data
  })
}

// 更新报销申请
export function updateExpenses(data) {
  return request({
    url: '/oa/expenses/update',
    method: 'put',
    data: data
  })
}

// 删除报销申请
export function deleteExpenses(id) {
  return request({
    url: '/oa/expenses/delete?id=' + id,
    method: 'delete'
  })
}

// 获得报销申请
export function getExpenses(id) {
  return request({
    url: '/oa/expenses/get?id=' + id,
    method: 'get'
  })
}

// 获得报销申请分页
export function getExpensesPage(query) {
  return request({
    url: '/oa/expenses/page',
    method: 'get',
    params: query
  })
}

// 导出报销申请 Excel
export function exportExpensesExcel(query) {
  return request({
    url: '/oa/expenses/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
