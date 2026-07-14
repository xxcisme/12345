export function downloadBlob(blob, filename) {
    if (window.navigator && window.navigator.msSaveOrOpenBlob) {
        window.navigator.msSaveOrOpenBlob(blob, filename)
        return
    }

    const link = document.createElement('a')
    const url = URL.createObjectURL(blob)
    link.href = url
    link.download = filename
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    URL.revokeObjectURL(url)
}

export function downloadByUrl(url, filename) {
    const link = document.createElement('a')
    link.href = url
    link.download = filename || ''
    link.target = '_blank'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
}

export async function downloadExcel(exportApi, params, filename) {
    const res = await exportApi(params)

    const disposition = res.headers?.['content-disposition']
    let downloadName = filename || `export_${Date.now()}.xls`

    if (disposition) {
        const match = disposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)
        if (match && match[1]) {
            downloadName = decodeURIComponent(match[1].replace(/['"]/g, ''))
        }
    }

    const blob = new Blob([res.data], {
        type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    downloadBlob(blob, downloadName)
}

export function downloadExcelFromBlob(blob, filename) {
    const name = filename || `export_${Date.now()}.xls`
    downloadBlob(
        new Blob([blob], {
            type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        }),
        name
    )
}